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
