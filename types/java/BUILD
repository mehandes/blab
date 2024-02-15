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
    test_class = "org.blab.types.DelimitedMapperTest",
    deps = [
        ":core",
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "JsonMapperTest",
    srcs = ["src/test/java/org/blab/types/JsonMapperTest.java"],
    test_class = "org.blab.types.JsonMapperTest",
    deps = [
        ":core",
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "CsvMapperTest",
    srcs = ["src/test/java/org/blab/types/CsvMapperTest.java"],
    test_class = "org.blab.types.CsvMapperTest",
    deps = [
        ":core",
        "@maven//:junit_junit",
    ],
)
