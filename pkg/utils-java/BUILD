package(default_visibility = ["//visibility:public"])

java_library(
    name = "buffer",
    srcs = ["src/main/java/org/blab/utils/RingBuffer.java"],
)

java_library(
    name = "net",
    srcs = glob(["src/main/java/org/blab/utils/net/*.java"]),
    deps = [
        ":buffer",
    ],
)
