CREATE TABLE mocker.rule_tag (
	id varchar NOT NULL,
	"value" varchar NOT NULL,
	CONSTRAINT rule_tag_pk PRIMARY KEY (id)
);

CREATE TABLE mocker.resource_tag (
	id varchar NOT NULL,
	"value" varchar NOT NULL,
	CONSTRAINT resource_tag_pk PRIMARY KEY (id)
);

CREATE TABLE mocker.archetype (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	subsystem_url varchar NOT NULL,
	resource_url varchar NOT NULL,
	http_method varchar NOT NULL,
	CONSTRAINT archetype_pk PRIMARY KEY (id),
	CONSTRAINT archetype_u UNIQUE (subsystem_url, resource_url, http_method)
);

CREATE TABLE mocker.archetype_parameter (
    archetype_id varchar NOT NULL,
    "name" varchar NOT NULL,
    "type" varchar NOT NULL,
	CONSTRAINT archetype_parameter_pk PRIMARY KEY (archetype_id, "name"),
	CONSTRAINT archetype_parameter_archetype_fk FOREIGN KEY (archetype_id) REFERENCES mocker.archetype(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.archetype_tag (
	archetype_id varchar NOT NULL,
	tag_id varchar NOT NULL,
	CONSTRAINT archetype_tag_pk PRIMARY KEY (archetype_id,tag_id),
	CONSTRAINT archetype_tag_archetype_fk FOREIGN KEY (archetype_id) REFERENCES mocker.archetype(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT archetype_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES mocker.resource_tag(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.archetype_schema (
    id varchar NOT NULL,
	name varchar NOT NULL,
	subsystem varchar NOT NULL,
	content varchar NOT NULL,
	CONSTRAINT archetype_schema_pk PRIMARY KEY (id),
	CONSTRAINT archetype_schema_u UNIQUE (name, subsystem)
);

CREATE TABLE mocker.archetype_response (
	id varchar NOT NULL,
	archetype_id varchar NOT NULL,
	status int8 NOT NULL,
	schema_id varchar,
	CONSTRAINT archetype_response_pk PRIMARY KEY (id),
	CONSTRAINT archetype_response_archetype_fk FOREIGN KEY (archetype_id) REFERENCES mocker.archetype(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT archetype_response_archetype_schema_fk FOREIGN KEY (schema_id) REFERENCES mocker.archetype_schema(id) ON DELETE SET NULL ON UPDATE SET NULL
);

CREATE TABLE mocker.archetype_response_header (
    archetype_response_id varchar NOT NULL,
    "header" varchar NOT NULL,
    "value" varchar NOT NULL,
    CONSTRAINT archetype_response_header_pk PRIMARY KEY (archetype_response_id,"header"),
    CONSTRAINT archetype_response_header_fk FOREIGN KEY (archetype_response_id) REFERENCES mocker.archetype_response(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.mock_resource (
	id int8 NOT NULL,
	subsystem_url varchar NOT NULL,
	resource_url varchar NOT NULL,
	http_method varchar NOT NULL,
	"name" varchar NOT NULL,
	is_active boolean NOT NULL,
	archetype_id varchar,
	CONSTRAINT mock_resource_pk PRIMARY KEY (id),
    CONSTRAINT mock_resource_archetype_fk FOREIGN KEY (archetype_id) REFERENCES mocker.archetype(id) ON DELETE SET NULL ON UPDATE SET NULL
);

CREATE TABLE mocker.mock_response (
	id varchar NOT NULL,
	body varchar NULL,
	status int8 NOT NULL,
	CONSTRAINT mock_response_pk PRIMARY KEY (id)
);

CREATE TABLE mocker.mock_rule (
	id varchar NOT NULL,
	"name" varchar NOT NULL,
	"order" int8 NOT NULL,
	is_active boolean NOT NULL,
	resource_id int8 NOT NULL,
	response_id varchar NOT NULL,
	CONSTRAINT mock_rule_pk PRIMARY KEY (id),
	CONSTRAINT mock_rule_mock_resource_fk FOREIGN KEY (resource_id) REFERENCES mocker.mock_resource(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT mock_rule_mock_response_fk FOREIGN KEY (response_id) REFERENCES mocker.mock_response(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.mock_condition (
	id varchar NOT NULL,
	"order" int8 NOT NULL,
	field_position varchar NOT NULL,
	content_type varchar NOT NULL,
	field_name varchar NOT NULL,
	condition_type varchar NOT NULL,
	condition_value varchar NULL,
	rule_id varchar NOT NULL,
	CONSTRAINT mock_condition_pk PRIMARY KEY (id),
	CONSTRAINT mock_condition_fk FOREIGN KEY (rule_id) REFERENCES mocker.mock_rule(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.response_header (
	response_id varchar NOT NULL,
	"header" varchar NOT NULL,
	"value" varchar NOT NULL,
	CONSTRAINT response_header_pk PRIMARY KEY (response_id,"header"),
	CONSTRAINT response_header_fk FOREIGN KEY (response_id) REFERENCES mocker.mock_response(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.injectable_parameter (
	response_id varchar NOT NULL,
	"parameter" varchar NOT NULL,
	CONSTRAINT injectable_parameter_pk PRIMARY KEY (response_id,"parameter"),
	CONSTRAINT injectable_parameter_fk FOREIGN KEY (response_id) REFERENCES mocker.mock_response(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.mock_resource_tag (
	mock_resource_id int8 NOT NULL,
	tag_id varchar NOT NULL,
	CONSTRAINT mock_resource_tag_pk PRIMARY KEY (mock_resource_id,tag_id),
	CONSTRAINT mock_resource_tag_mock_resource_fk FOREIGN KEY (mock_resource_id) REFERENCES mocker.mock_resource(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT mock_resource_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES mocker.resource_tag(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE mocker.mock_rule_tag (
	mock_rule_id varchar NOT NULL,
	tag_id varchar NOT NULL,
	CONSTRAINT mock_rule_tag_pk PRIMARY KEY (mock_rule_id,tag_id),
	CONSTRAINT mock_rule_tag_mock_rule_fk FOREIGN KEY (mock_rule_id) REFERENCES mocker.mock_rule(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT mock_rule_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES mocker.rule_tag(id) ON DELETE CASCADE ON UPDATE CASCADE
);