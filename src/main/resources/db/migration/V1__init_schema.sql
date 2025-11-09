-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Create documents table
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    extracted_text TEXT,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create embedding_vectors table
CREATE TABLE embedding_vectors (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    embedding_vector vector(1536),
    chunk_index INTEGER,
    CONSTRAINT fk_embedding_document FOREIGN KEY (document_id) REFERENCES documents(id)
);

-- Create query_history table
CREATE TABLE query_history (
    id BIGSERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create query_context_docs table for storing context documents
CREATE TABLE query_context_docs (
    query_id BIGINT NOT NULL REFERENCES query_history(id) ON DELETE CASCADE,
    document_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (query_id, document_name)
);

-- Create indexes for better performance
CREATE INDEX idx_documents_uploaded_at ON documents(uploaded_at DESC);
CREATE INDEX idx_embedding_vectors_document_id ON embedding_vectors(document_id);
CREATE INDEX idx_query_history_created_at ON query_history(created_at DESC);

-- Create index for vector similarity search
CREATE INDEX idx_embedding_vectors_cosine ON embedding_vectors USING ivfflat (embedding_vector vector_cosine_ops) WITH (lists = 100);