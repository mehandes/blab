java_library(
    name = "buffer",
    srcs = ["src/main/java/org/blab/utils/Buffer.java"],
)

java_library(
    name = "socket-channel",
    srcs = ["src/main/java/org/blab/utils/net/BlockingSocketChannel.java"],
)

java_test(
    name = "BufferTest",
    srcs = ["src/test/java/org/blab/utils/BufferTest.java"],
    test_class = "org.blab.utils.BufferTest",
    deps = [
        ":buffer",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)
