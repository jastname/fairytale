package io.github.jastname.fairytale.rag.service;

public interface EmbeddingService {

	String chunkAndEmbedAllFairytales();

	double[] embed(String text);

	String modelName();

}