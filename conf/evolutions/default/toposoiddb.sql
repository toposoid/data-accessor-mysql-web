DROP TABLE IF EXISTS `toposoiddb.knowledge_register_history`;
DROP TABLE IF EXISTS `toposoiddb.document_analysis_result_history`;
DROP TABLE IF EXISTS `toposoiddb.knowledge_register_states`;

CREATE TABLE toposoiddb.document_analysis_result_history (
    id serial NOT NULL PRIMARY KEY,
    user_id VARCHAR(1024) NOT NULL,
    document_id VARCHAR(1024) NOT NULL,
    original_filename TEXT NOT NULL,
    total_separated_number INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE toposoiddb.knowledge_register_history (
    id serial NOT NULL PRIMARY KEY,
    user_id VARCHAR(1024) NOT NULL,
    state_id BIGINT NOT NULL,
    document_id VARCHAR(1024) NOT NULL,
    sequential_number INT NOT NULL,
    proposition_id VARCHAR(1024) NOT NULL,
    sentences TEXT NOT NULL,
    json TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE toposoiddb.knowledge_register_states (
    id serial NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO toposoiddb.knowledge_register_states (name) VALUES ("success");
INSERT INTO toposoiddb.knowledge_register_states (name) VALUES ("failure");
