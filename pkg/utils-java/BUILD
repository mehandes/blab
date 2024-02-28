package(default_visibility = ["//visibility:public"])

java_library(
    name = "buffer",
    srcs = ["src/main/java/org/blab/utils/RingBuffer.java"],
)

java_library(
    name = "net",
    srcs = ["src/main/java/org/blab/utils/net/*.java"],
    deps = [
        ":buffer",
    ],
)

java_binary(
    name = "BlockingSocketChannelTest",
    srcs = ["src/test/java/org/blab/utils/BlockingSocketChannelTest.java"],
    main_class = "org.blab.utils.BlockingSocketChannelTest",
    deps = [
        ":net",
    ],
)

java_test(
    name = "RingBufferTest",
    srcs = ["src/test/java/org/blab/utils/RingBufferTest.java"],
    deps = [
        ":buffer",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)
