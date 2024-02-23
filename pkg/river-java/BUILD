java_library(
    name = "river-java",
    srcs = glob(["src/main/java/org/blab/river/*.java"]),
)

java_library(
    name = "vcas",
    srcs = glob(["src/main/java/org/blab/river/vcas/*.java"]),
    deps = [
        ":river-java",
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
