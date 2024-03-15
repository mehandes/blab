java_library(
    name = "registry",
    srcs = glob(["src/main/java/org/blab/blender/registry/*.java"]),
    deps = [
        "@maven//:org_apache_avro_avro"
    ]
)

java_test(
  name = "SchemeTest",
  srcs = ["src/test/java/org/blab/blender/registry/SchemeTest.java"],
  deps = [
    "@maven//:org_junit_jupiter_junit_jupiter_api",
    "@maven//:junit_junit",
    "@maven//:org_apache_avro_avro",
    ":registry"
  ]
)

