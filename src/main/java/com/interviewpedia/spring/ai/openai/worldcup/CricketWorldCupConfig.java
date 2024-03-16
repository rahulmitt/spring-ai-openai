package com.interviewpedia.spring.ai.openai.worldcup;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class CricketWorldCupConfig {
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public VectorStore vector(EmbeddingClient embedding) {
        Resource pdf = resourceLoader.getResource("classpath:Cricket_World_Cup.pdf");
        VectorStore vectors = new SimpleVectorStore(embedding);
        var reader = new PagePdfDocumentReader(pdf);
        var splitter = new TokenTextSplitter();
        var documents = splitter.apply(reader.get());
        vectors.accept(documents);
        return vectors;
    }

    @Bean
    public ImageClient imageClient(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return new OpenAiImageClient(new OpenAiImageApi(apiKey));
    }
}



