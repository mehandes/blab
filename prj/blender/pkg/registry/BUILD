java_library(
    name = "registry",
    srcs = glob(["src/main/java/**/*.java"]),
    deps = [
        "@maven//:org_apache_avro_avro"
    ]
)

java_test(
    name = "PosixPatternValidatorTest",
    srcs = glob(["src/test/java/**/PosixPatternValidatorTest.java"]),
    test_class = "org.blab.blender.registry.PosixPatternValidatorTest",
    deps = [
        ":registry",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_test(
    name = "AvroSchemaValidatorTest",
    srcs = glob(["src/test/java/**/AvroSchemaValidatorTest.java"]),
    test_class = "org.blab.blender.registry.AvroSchemaValidatorTest",
    deps = [
        ":registry",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_apache_avro_avro"
    ],
)

java_test(
    name = "SchemaRecordRepositoryTest",
    srcs = glob(["src/test/java/**/SchemaRecordRepositoryTest.java"]),
    test_class = "org.blab.blender.registry.SchemaRecordRepositoryTest",
    deps = [
        ":registry",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_apache_avro_avro"
    ],
)

java_test(
    name = "RegistryTest",
    srcs = glob(["src/test/java/**/RegistryTest.java"]),
    test_class = "org.blab.blender.registry.RegistryTest",
    deps = [
        ":registry",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_apache_avro_avro"
    ],
)
