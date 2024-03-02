package(default_visibility = ["//visibility:public"])

java_library(
    name = "river-java",
    srcs = glob(["src/main/java/org/blab/river/*.java"]),
)

java_library(
    name = "vcas",
    srcs = glob(["src/main/java/org/blab/river/vcas/*.java"]),
    deps = [
        ":river-java",
        "//pkg/utils-java:buffer",
        "//pkg/utils-java:net",
    ],
)

java_library(
    name = "mqtt",
    srcs = glob(["src/main/java/org/blab/river/mqtt/*.java"]),
    deps = [
        ":river-java",
        "//pkg/utils-java:buffer",
        "@maven//:org_eclipse_paho_org_eclipse_paho_mqttv5_client",
    ],
)
