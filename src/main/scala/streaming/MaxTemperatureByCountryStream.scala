package streaming


import org.apache.spark.SparkContext
import org.apache.spark.sql.{SparkSession, types}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
/**
  * Created by Marcos Neco on 15/07/2017.
  *
  * This example show how to do streaming in some path to calculate max averageTemperature grouped by country
  * using spark streaming api
  */
class MaxTemperatureByCountryStream(spark: SparkSession, sc: SparkContext) {

  val path =  "E:/data_analyze/global_temperature/*"

  def getSchema(): StructType = {
    StructType(StructField("date", StringType) ::
                            StructField("averageTemperature", FloatType) ::
                            StructField("averageTemperatureUncert", StringType) ::
                            StructField("city", StringType)::
                            StructField("country", StringType) ::
                            StructField("latitude", StringType) ::
                            StructField("longitude", StringType) :: Nil)
  }

  def useStreaming() = {
    val streamGlobalTemperature = spark
                                .readStream
                                .option("maxFilesPerTrigger", 1)
                                .schema(getSchema())
                                .format("csv")
                                .load(path)


    val groupedByCountry = streamGlobalTemperature
      .selectExpr("*")
      .groupBy("country")
      .max("averageTemperature")
      .orderBy(desc("max(averageTemperature)"))

    groupedByCountry.writeStream
                    .format("memory")
                    .queryName("global_temperature")
                    .outputMode("complete")
                    .start()

    spark.sql("select * from global_temperature").take(10).foreach(println)
  }
}
