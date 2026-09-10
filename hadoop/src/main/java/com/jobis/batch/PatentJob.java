package com.jobis.batch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class PatentJob {

    private PatentJob() {
    }

    enum Counter {
        INVALID_RECORD,
        MATCHED_RECORD
    }

    public static final class PatentMapper extends Mapper<LongWritable, Text, Text, Text> {

        private PatentMatcher matcher;

        @Override
        protected void setup(Context context) throws IOException {
            URI[] cacheFiles = context.getCacheFiles();
            if (cacheFiles == null || cacheFiles.length != 1) {
                throw new IOException("기업 기준정보 CSV 파일을 정확히 하나 지정해야 합니다.");
            }

            URI companyFile = cacheFiles[0];
            FileSystem fileSystem = FileSystem.get(companyFile, context.getConfiguration());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fileSystem.open(new Path(companyFile)),
                    StandardCharsets.UTF_8
            ))) {
                matcher = PatentMatcher.fromCompanyCsv(reader);
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            try {
                var match = matcher.match(value.toString());
                if (match.isPresent()) {
                    context.write(
                            new Text(match.get().companyKey()),
                            new Text(match.get().patentCsv())
                    );
                    context.getCounter(Counter.MATCHED_RECORD).increment(1);
                }
            } catch (IOException | IllegalArgumentException exception) {
                context.getCounter(Counter.INVALID_RECORD).increment(1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println(
                    "Usage: PatentJob <patent-input> <company-reference-csv> <output>"
            );
            System.exit(2);
        }

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "Jobis patent-company matching");
        job.setJarByClass(PatentJob.class);
        job.setMapperClass(PatentMapper.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.addCacheFile(URI.create(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
