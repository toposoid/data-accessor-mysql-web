/*
 * Copyright 2021 Linked Ideal LLC.[https://linked-ideal.com/]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DROP TABLE IF EXISTS `knowledge_register_history`;
DROP TABLE IF EXISTS `document_analysis_result_history`;
DROP TABLE IF EXISTS `knowledge_register_states`;
DROP TABLE IF EXISTS `document_analysis_result_states`;
DROP TABLE IF EXISTS `non_sentence_sections`;
DROP TABLE IF EXISTS `non_sentence_types`;

CREATE TABLE toposoiddb.document_analysis_result_history (
    id serial NOT NULL PRIMARY KEY,
    user_id VARCHAR(512) NOT NULL,
    state_id BIGINT NOT NULL,
    document_id VARCHAR(512) NOT NULL,
    original_filename TEXT NOT NULL,
    total_separated_number INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE toposoiddb.document_analysis_result_states (
    id serial NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE toposoiddb.knowledge_register_history (
    id serial NOT NULL PRIMARY KEY,
    user_id VARCHAR(512) NOT NULL,
    state_id BIGINT NOT NULL,
    document_id VARCHAR(512) NOT NULL,
    sequential_number INT NOT NULL,
    proposition_id VARCHAR(512) NOT NULL,
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


CREATE TABLE toposoiddb.non_sentence_sections (
    id serial NOT NULL PRIMARY KEY,
    document_id VARCHAR(512) NOT NULL,
    page_no INT NOT NULL,
    non_sentence_type BIGINT NOT NULL,
    non_sentence TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE toposoiddb.non_sentence_types (
    id serial NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE INDEX document_analysis_result_history_user_id_idx on toposoiddb.document_analysis_result_history(user_id);
CREATE INDEX document_analysis_result_history_document_id_idx on toposoiddb.document_analysis_result_history(document_id);
CREATE INDEX knowledge_register_history_user_id_idx on toposoiddb.knowledge_register_history(user_id);
CREATE INDEX knowledge_register_history_document_id_idx on toposoiddb.knowledge_register_history(document_id);
CREATE INDEX knowledge_register_history_proposition_id_idx on toposoiddb.knowledge_register_history(proposition_id);
CREATE INDEX non_sentence_sections_document_id_idx on toposoiddb.non_sentence_sections(document_id);
CREATE INDEX non_sentence_sections_non_sentence_type_idx on toposoiddb.non_sentence_sections(non_sentence_type);

INSERT INTO toposoiddb.document_analysis_result_states (name) VALUES ("success");
INSERT INTO toposoiddb.document_analysis_result_states (name) VALUES ("failure");
INSERT INTO toposoiddb.document_analysis_result_states (name) VALUES ("upload completed");
INSERT INTO toposoiddb.document_analysis_result_states (name) VALUES ("validation completed");
INSERT INTO toposoiddb.document_analysis_result_states (name) VALUES ("analysis completed");

INSERT INTO toposoiddb.knowledge_register_states (name) VALUES ("success");
INSERT INTO toposoiddb.knowledge_register_states (name) VALUES ("failure");

INSERT INTO toposoiddb.non_sentence_types (name) VALUES ("unspecified");
INSERT INTO toposoiddb.non_sentence_types (name) VALUES ("references");
INSERT INTO toposoiddb.non_sentence_types (name) VALUES ("table of contents");
INSERT INTO toposoiddb.non_sentence_types (name) VALUES ("headlines");
INSERT INTO toposoiddb.non_sentence_types (name) VALUES ("title of top page");
