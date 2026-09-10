from pydantic import BaseModel, ConfigDict, Field, HttpUrl


class NewsArticle(BaseModel):
    model_config = ConfigDict(extra="forbid")

    title: str
    content: str
    date: str | None = None
    link: HttpUrl


class KeywordInsight(BaseModel):
    model_config = ConfigDict(extra="forbid")

    keyword: str = Field(min_length=1, max_length=13)
    importance: int = Field(ge=0, le=10)
    sentiment: bool


class NewsAnalysis(BaseModel):
    model_config = ConfigDict(extra="forbid")

    keywords: list[KeywordInsight] = Field(max_length=10)


class NewsResult(BaseModel):
    model_config = ConfigDict(extra="forbid")

    news: list[NewsArticle]
    keywords: list[KeywordInsight]
