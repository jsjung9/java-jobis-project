import js from '@eslint/js';
import pluginVue from 'eslint-plugin-vue';
import prettier from '@vue/eslint-config-prettier';
import globals from 'globals';

export default [
    { ignores: ['dist/**', 'node_modules/**'] },
    js.configs.recommended,
    ...pluginVue.configs['flat/essential'],
    prettier,
    {
        files: ['eslint.config.js'],
        languageOptions: {
            globals: globals.node,
        },
    },
    {
        files: ['**/*.{js,vue}'],
        languageOptions: {
            ecmaVersion: 'latest',
            sourceType: 'module',
            globals: globals.browser,
        },
        rules: {
            'no-var': 'error',
            quotes: ['error', 'single'],
            semi: ['error', 'always'],
        },
    },
];
