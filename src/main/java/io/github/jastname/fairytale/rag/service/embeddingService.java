package io.github.jastname.fairytale.rag.service;

public interface embeddingService {

	String chunkAndEmbedAllFairytales();

	double[] embed(String text);

	String modelName();

}