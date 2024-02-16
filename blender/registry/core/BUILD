java_library(
    name = "core",
    srcs = glob(["src/main/java/**/*.java"]),
)

java_test(
    name = "PosixPatternValidatorTest",
    srcs = glob(["src/test/java/**/PosixPatternValidatorTest.java"]),
    test_class = "org.blab.blender.registry.PosixPatternValidatorTest",
    deps = [
        ":core",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)