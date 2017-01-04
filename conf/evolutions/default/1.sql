
# --- !Ups

CREATE TABLE "PERSON" ("id" SERIAL, "name" VARCHAR(255) NOT NULL, "age" BIGINT NOT NULL);

# --- !Downs

DROP TABLE "PERSON";
  