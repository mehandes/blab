java_library(
    name = "registry",
    srcs = glob(["src/main/java/org/blab/blender/registry/*.java"]),
    deps = [
        "@maven//:org_apache_avro_avro",
        "@maven//:org_postgresql_postgresql",
    ],
)

java_test(
    name = "SchemeTest",
    srcs = ["src/test/java/org/blab/blender/registry/SchemeTest.java"],
    deps = [
        ":registry",
        "@maven//:junit_junit",
        "@maven//:org_apache_avro_avro",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "ManualTest",
    srcs = ["src/test/java/org/blab/blender/registry/ManualTest.java"],
    deps = [
        ":registry",
        "@maven//:junit_junit",
        "@maven//:org_apache_avro_avro",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_mockito_mockito_core",
    ],
)
