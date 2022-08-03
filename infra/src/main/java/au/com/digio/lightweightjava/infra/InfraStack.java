package au.com.digio.lightweightjava.infra;

import software.amazon.awscdk.core.BundlingOptions;
import software.amazon.awscdk.core.CfnOutput;
import software.amazon.awscdk.core.CfnOutputProps;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.DockerImage;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.apigatewayv2.AddRoutesOptions;
import software.amazon.awscdk.services.apigatewayv2.DomainMappingOptions;
import software.amazon.awscdk.services.apigatewayv2.DomainName;
import software.amazon.awscdk.services.apigatewayv2.DomainNameAttributes;
import software.amazon.awscdk.services.apigatewayv2.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.HttpApiProps;
import software.amazon.awscdk.services.apigatewayv2.HttpMethod;
import software.amazon.awscdk.services.apigatewayv2.integrations.HttpLambdaIntegration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.assets.AssetOptions;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.core.BundlingOutput.NOT_ARCHIVED;

public class InfraStack extends Stack {
    public InfraStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public InfraStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        List<String> functionPackagingInstructions = Arrays.asList(
                "-c",
                "cd app "
                + "&& gradle nativeCompile "
                + "&& cp /asset-input/build/native/nativeCompile/app /asset-output/"
                + "&& cp ../config/bootstrap /asset-output/"
        );

        BundlingOptions.Builder builderOptions = BundlingOptions.builder()
                .command(functionPackagingInstructions)
                .image(DockerImage.fromRegistry("jasonfungsing/al2-graalvm"))
                .user("root")
                .outputType(NOT_ARCHIVED);

        Function getAirportsFunction = new Function(this, "GetAirports", FunctionProps.builder()
                .runtime(Runtime.PROVIDED_AL2)
                .code(Code.fromAsset("../", AssetOptions.builder()
                        .bundling(builderOptions.build())
                        .build()))
                .handler("CFAirports")
                .memorySize(1024)
                .timeout(Duration.seconds(10))
                .logRetention(RetentionDays.ONE_WEEK)
                .build());

        HttpApi httpApi = new HttpApi(this, "GetAirportsSpringNativeOnLambdaAPI", HttpApiProps.builder()
                .apiName("GetAirportsSpringNativeLambdaAPI")
                .defaultDomainMapping(DomainMappingOptions.builder()
                        .domainName(DomainName.fromDomainNameAttributes(this, "GetAirportsSpringNativeLambdaDomain",
                                        DomainNameAttributes
                                                .builder()
                                                .name("xxxxxx.sandbox.digio.com.au")
                                                .regionalDomainName("xxxxxx.sandbox.digio.com.au")
                                                .regionalHostedZoneId("xxxxxxxxxxxxx")
                                                .build()
                                )
                        )
                        .mappingKey("spring-native").build())
                .build());

        HttpLambdaIntegration getAirportsIntegration = new HttpLambdaIntegration("GetAirportsIntegration", getAirportsFunction);

        httpApi.addRoutes(AddRoutesOptions.builder()
                .path("/airports")
                .methods(singletonList(HttpMethod.GET))
                .integration(getAirportsIntegration)
                .build());

        CfnOutput apiUrl = new CfnOutput(this, "GetAirportsUrl", CfnOutputProps.builder()
                .exportName("GetAirportsUrl")
                .value(httpApi.getApiEndpoint())
                .build());
    }
}
