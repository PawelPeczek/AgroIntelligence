package server;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import json_api.*;
import model.entities.Field;
import model.entities.Sensor;
import model.entities.Strategy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


import static akka.http.javadsl.server.Directives.path;
import static akka.http.javadsl.server.Directives.pathPrefix;
import static akka.http.javadsl.server.Directives.route;
import static akka.http.javadsl.server.PathMatchers.integerSegment;


public class HTTPServer extends AllDirectives {
    public static void main(String[] args) throws Exception {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //In order to access all directives we need an instance where the routes are define.
        HTTPServer app = new HTTPServer();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private Route createRoute() {
        return route(
                pathPrefix("field", () -> route(
                        pathPrefix("getById", () ->
                                path(integerSegment(), id -> route(
                                        get(() -> {
                                                    final CompletionStage<Optional<Field>> futMaybeResult = RouterHelpers.fetchField(id);
                                                    return onSuccess(() -> futMaybeResult, maybeResult ->
                                                            maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                                    );
                                                }
                                        ))
                                )
                        ),
                        path("fetchAllFields", () ->
                                get(() -> {
                                            final CompletionStage<Optional<List<Field>>> futMaybeResult = RouterHelpers.fetchAllFields();
                                            return onSuccess(() -> futMaybeResult, maybeResult ->
                                                    maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                            .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not found"))
                                            );
                                        })

                        ),
                        pathPrefix("getCannonsByFieldId", () ->
                                path(integerSegment(), id -> route(
                                        get(() -> {
                                                    final CompletionStage<Optional<List<WaterCannonWrapper>>> futMaybeResult =
                                                            RouterHelpers.fetchCannonsByFieldId(id);
                                                    return onSuccess(() -> futMaybeResult, maybeResult ->
                                                            maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                                    );
                                                }
                                        ))
                                )
                        ),
                        pathPrefix("getCurrentStrategyByFieldId", () ->
                                path(integerSegment(), id -> route(
                                        get(() -> {
                                                    final CompletionStage<Optional<Strategy>> futMaybeResult =
                                                            RouterHelpers.fetchCurrentStrategy(id);
                                                    return onSuccess(() -> futMaybeResult, maybeResult ->
                                                            maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                                    );
                                                }
                                        ))
                                )
                        ),
                        pathPrefix("getStrategiesByFieldId", () ->
                                path(integerSegment(), id -> route(
                                        get(() -> {
                                                    final CompletionStage<Optional<List<Strategy>>> futMaybeResult =
                                                            RouterHelpers.fetchStrategiesByFieldId(id);
                                                    return onSuccess(() -> futMaybeResult, maybeResult ->
                                                            maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                                    );
                                                }
                                        ))
                                )
                        ),
                        path("createField", () ->
                                post(() ->
                                        entity(Jackson.unmarshaller(Field.class), field -> {
                                        final CompletionStage<Optional<Field>> futMaybeResult = RouterHelpers.saveField(field);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                           .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                                )
                        ),
                        path("editField", () ->
                                post(() ->
                                        entity(Jackson.unmarshaller(Field.class), field -> {
                                            final CompletionStage<Optional<Field>> futMaybeResult = RouterHelpers.editField(field);
                                            return onSuccess(() -> futMaybeResult, maybeResult ->
                                                    maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                            .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                            );
                                        })
                                )
                        )
            )),
            pathPrefix("measurements", () -> route(
                    pathPrefix("getLatestForSensorId", () ->
                            path(integerSegment(), id -> route(
                                    get(() -> {
                                                final CompletionStage<Optional<HumidityWrapper>> futMaybeResult = RouterHelpers.fetchLatestMeasurement(id);
                                                return onSuccess(() -> futMaybeResult, maybeResult ->
                                                        maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                                .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                                );
                                            }
                                    ))
                            )
                    ),
                    path("getAllMeasurements", () ->
                            get(() -> {
                                final CompletionStage<Optional<List<HumidityWrapper>>> futMaybeResult = RouterHelpers.fetchAllMeasurements();
                                return onSuccess(() -> futMaybeResult, maybeResult ->
                                        maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not found"))
                                );
                            })

                    ),
                    path("addMeasurement", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(HumidityWrapper.class), measurement -> {
                                        final CompletionStage<Optional<HumidityWrapper>> futMaybeResult = RouterHelpers.saveMeasurement(measurement);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    )
            )),
            pathPrefix("sensors", () -> route(
                    path("createSensor", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(ListOfIDs.class), list -> {
                                        final CompletionStage<Optional<Sensor>> futMaybeResult = RouterHelpers.saveSensor(list);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    )
            )),
            pathPrefix("strategies", () -> route(
                    path("createStrategy", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(Strategy.class), strategy -> {
                                        final CompletionStage<Optional<Strategy>> futMaybeResult = RouterHelpers.saveStrategy(strategy);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    ),
                    path("addStrategyToField", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(DoubleIDMessage.class), message -> {
                                        final CompletionStage<Optional<Strategy>> futMaybeResult = RouterHelpers.addStrategyToField(message);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    )
            )),
            pathPrefix("waterCannons", () -> route(
                    path("createCannon", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(WaterCannonWrapper.class), cannon -> {
                                        final CompletionStage<Optional<WaterCannonWrapper>> futMaybeResult =
                                                RouterHelpers.saveCannon(cannon);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    )   ,
                    path("addCannonToField", () ->
                            post(() ->
                                    entity(Jackson.unmarshaller(DoubleIDMessage.class), message -> {
                                        final CompletionStage<Optional<WaterCannonWrapper>> futMaybeResult =
                                                RouterHelpers.addCannonToField(message);
                                        return onSuccess(() -> futMaybeResult, maybeResult ->
                                                maybeResult.map(res -> completeOK(res, Jackson.marshaller()))
                                                        .orElseGet(() -> complete(StatusCodes.BAD_REQUEST, "Cannot save"))
                                        );
                                    })
                            )
                    )
            ))
        );

    }
}
