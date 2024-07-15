import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator
import org.example.market.ai.LstmModel
import org.nd4j.evaluation.regression.RegressionEvaluation
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize
import org.nd4j.linalg.factory.Nd4j

class Predictor(private val model: LstmModel, private val data: List<Double>, private val lookback: Int = data.size) {


    fun loadData(): Pair<Array<INDArray>, INDArray> {

        val maxPrice = data.maxOrNull() ?: 1.0
        val normalizedPrices = data.map { it / maxPrice }

        val features = mutableListOf<INDArray>()
        val labels = mutableListOf<Double>()

        for (i in lookback until normalizedPrices.size) {
            val featureArray = Nd4j.create(normalizedPrices.subList(i - lookback, i).toDoubleArray())
            features.add(featureArray.reshape(lookback.toLong(), 1))
            labels.add(normalizedPrices[i])
        }

        val featureArray = Nd4j.concat(0, *features.toTypedArray())
        val labelArray = Nd4j.create(labels.toDoubleArray())

        return Pair(arrayOf(featureArray), labelArray)
    }

    fun train(epochs: Int = 10, batchSize: Int = 32) {
        val (features, labels) = loadData()
        val dataSet = DataSet(features[0], labels)

        val iterator: DataSetIterator = ListDataSetIterator(dataSet.asList(), batchSize)
        val normalizer: DataNormalization = NormalizerStandardize()
        normalizer.fit(iterator)
        iterator.reset()

        normalizer.transform(dataSet)

        for (i in 0 until epochs) {
            while (iterator.hasNext()) {
                model.fit(iterator.next()) // Assuming MyModel has a fit method
            }
            iterator.reset()
        }
    }

    fun evaluate(): Double {
        val (features, labels) = loadData()
        val dataSet = DataSet(features[0], labels)

        val iterator: DataSetIterator = ListDataSetIterator(dataSet.asList(), 1)

        val eval = RegressionEvaluation(1)
        while (iterator.hasNext()) {
            val next = iterator.next()
            val output = model.output(next.features) // Assuming MyModel has an output method
            eval.eval(next.labels, output)
        }

        return eval.meanAbsoluteError(0)
    }

    fun predictNextPrice(recentPrices: List<Double>): Double {
        val maxPrice = recentPrices.maxOrNull() ?: 1.0
        val normalizedPrices = recentPrices.map { it / maxPrice }
        val inputArray = normalizedPrices.toDoubleArray()
        val input = Nd4j.create(inputArray).reshape(1, lookback.toLong(), 1)

        val predicted = model.output(input) // Assuming MyModel has an output method
        val prediction = predicted.getDouble(predicted.length() - 1) * maxPrice

        return prediction
    }
}
