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
        "//pkg/utils-java:socket-channel",
    ],
)

# java_library(
#     name = "mqtt",
#     srcs = glob(["src/main/java/org/blab/river/mqtt/*.java"]),
#     deps = [
#         ":river-java"
#     ]
# )

java_binary(
    name = "VcasConsumerTest",
    srcs = ["src/test/java/org/blab/river/vcas/VcasConsumerTest.java"],
    main_class = "org.blab.river.vcas.VcasConsumerTest",
    deps = [
        ":river-java",
        ":vcas",
    ],
)
