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
import static akka.http.javadsl.server.Directives.path;
import static akka.http.javadsl.server.Directives.pathEnd;
import static akka.http.javadsl.server.Directives.pathPrefix;
import static akka.http.javadsl.server.Directives.route;
import static akka.http.javadsl.server.Directives.parameter;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import json_api.DoubleIDMessage;
import json_api.HumidityWrapper;
import json_api.ListOfIDs;
import json_api.WaterCannonWrapper;
import model.dao.*;
import model.entities.Field;
import model.entities.Sensor;
import model.entities.Strategy;
import model.entities.WaterCannon;
import scala.collection.immutable.List$;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.http.javadsl.unmarshalling.StringUnmarshallers.INTEGER;

public class RouterHelpers {

    public static CompletionStage<Optional<Field>> fetchField(int fieldId){
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.getFieldByID(fieldId));
    }

    public static CompletionStage<Optional<Field>> saveField(final Field field) {
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.createField(field.getName()));
    }

    public static CompletionStage<Optional<Field>> editField(final Field field) {
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.editField(field.getId(), field.getName()));
    }

    public static CompletionStage<Optional<List<Field>>> fetchAllFields() {
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.getAllFields());
    }

    public static CompletionStage<Optional<List<WaterCannonWrapper>>> fetchCannonsByFieldId(int fieldId){
        final FieldDAO dao = new FieldDAO();
        Optional<List<WaterCannonWrapper>> result =
                dao.getAllCannons(fieldId)
                        .map(l -> l.stream().map(
                                WaterCannonWrapper::wrapWaterCannon)
                                .collect(Collectors.toList()));
        return CompletableFuture.completedFuture(result);
    }

    public static CompletionStage<Optional<List<Strategy>>> fetchStrategiesByFieldId(int fieldId){
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.getAllStrategies(fieldId));
    }

    public static CompletionStage<Optional<Strategy>> fetchCurrentStrategy(int fieldId) {
        final FieldDAO dao = new FieldDAO();
        return CompletableFuture.completedFuture(dao.getCurrentStrategy(fieldId));
    }

    public static CompletionStage<Optional<HumidityWrapper>> fetchLatestMeasurement(int sensorId) {
        final HumidityMeasurementDAO dao = new HumidityMeasurementDAO();
        return CompletableFuture.completedFuture(dao.getLatestMeasurement(sensorId).map(HumidityWrapper::wrapHumidity));
    }

    public static CompletionStage<Optional<HumidityWrapper>> saveMeasurement(final HumidityWrapper measurement) {
        final HumidityMeasurementDAO dao = new HumidityMeasurementDAO();
        return CompletableFuture.completedFuture(
                dao.addMeasurement(measurement.getTime(), measurement.getSensorId(), measurement.getValue())
                .map(HumidityWrapper::wrapHumidity));
    }

    public static CompletionStage<Optional<List<HumidityWrapper>>> fetchAllMeasurements(){
        final HumidityMeasurementDAO dao = new HumidityMeasurementDAO();
        Optional<List<HumidityWrapper>> result =
                dao.getAllMeasurements()
                        .map(l -> l.stream().map(
                                HumidityWrapper::wrapHumidity)
                                .collect(Collectors.toList()));
        return CompletableFuture.completedFuture(result);
    }

    public static CompletionStage<Optional<Sensor>> saveSensor(ListOfIDs listOfCannons){
        final SensorDAO dao = new SensorDAO();
        return CompletableFuture.completedFuture(dao.createSensor(listOfCannons.getEntities()));
    }

    public static CompletionStage<Optional<Strategy>> saveStrategy(Strategy strategy){
        final StrategyDAO dao = new StrategyDAO();
        return CompletableFuture.completedFuture(
                dao.addStrategy(strategy.getName(), strategy.getStartTime(),
                                strategy.getEndTime(), strategy.getMinHumidity(),
                                strategy.getMaxHumidity()));
    }

    public static CompletionStage<Optional<Strategy>> addStrategyToField(DoubleIDMessage message){
        final StrategyDAO dao = new StrategyDAO();
        return CompletableFuture.completedFuture(
                dao.addFieldToStrategy(message.getId_one(), message.getOd_two()));
    }

    public static CompletionStage<Optional<WaterCannonWrapper>> saveCannon(WaterCannonWrapper cannon){
        final WaterCannonDAO dao = new WaterCannonDAO();
        return CompletableFuture.completedFuture(
                dao.createCannon(cannon.getFieldId()).map(WaterCannonWrapper::wrapWaterCannon)
        );
    }

    public static CompletionStage<Optional<WaterCannonWrapper>> addCannonToField(DoubleIDMessage message){
        final WaterCannonDAO dao = new WaterCannonDAO();
        return CompletableFuture.completedFuture(
                dao.addCannonToField(message.getId_one(), message.getOd_two())
                   .map(WaterCannonWrapper::wrapWaterCannon));
    }
}
