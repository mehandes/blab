java_binary(
    name = "demo",
    srcs = glob(["src/main/java/**/*.java"]),
    main_class = "org.blab.lambda.demo.Demo",
    deps = [
        "//pkg/river-java",
        "//pkg/river-java:vcas",
        "@maven//:com_google_code_gson_gson",
        "@maven//:org_apache_commons_commons_math3",
    ],
)
