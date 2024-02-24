package(default_visibility = ["//visibility:public"])

java_library(
    name = "buffer",
    srcs = ["src/main/java/org/blab/utils/RingBuffer.java"],
)

java_library(
    name = "socket-channel",
    srcs = ["src/main/java/org/blab/utils/net/BlockingSocketChannel.java"],
    deps = [
        ":buffer",
    ],
)

java_binary(
    name = "BlockingSocketChannelTest",
    srcs = ["src/test/java/org/blab/utils/BlockingSocketChannelTest.java"],
    main_class = "org.blab.utils.BlockingSocketChannelTest",
    deps = [
        ":socket-channel",
    ],
)
