java_library(
    name = "types-java",
    srcs = glob(["src/main/java/org/blab/types/*.java"]),
    deps = [
        "@maven//:com_google_code_gson_gson",
        "@maven//:org_apache_avro_avro",
    ],
)

java_test(
    name = "DelimitedMapperTest",
    srcs = ["src/test/java/org/blab/types/DelimitedMapperTest.java"],
    test_class = "org.blab.types.DelimitedMapperTest",
    deps = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "JsonMapperTest",
    srcs = ["src/test/java/org/blab/types/JsonMapperTest.java"],
    test_class = "org.blab.types.JsonMapperTest",
    deps = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "CsvMapperTest",
    srcs = ["src/test/java/org/blab/types/CsvMapperTest.java"],
    test_class = "org.blab.types.CsvMapperTest",
    deps = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)
