java_library(
    name = "core",
    srcs = glob(["src/main/java/**/*.java"]),
    deps = [
        "@maven//:org_apache_avro_avro",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_test(
    name = "DelimitedMapperTest",
    srcs = ["src/test/java/org/blab/types/DelimitedMapperTest.java"],
    deps = [
        ":core",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "JsonMapperTest",
    srcs = ["src/test/java/org/blab/types/JsonMapperTest.java"],
    deps = [
        ":core",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "CsvMapperTest",
    srcs = ["src/test/java/org/blab/types/CsvMapperTest.java"],
    deps = [
        ":core",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)
