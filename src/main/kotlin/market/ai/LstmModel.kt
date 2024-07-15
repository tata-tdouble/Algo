package org.example.market.ai

import kotlinx.serialization.Serializable
import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.ConvexOptimizer;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.IEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet
import org.nd4j.linalg.dataset.api.MultiDataSet
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

class LstmModel : NeuralNetwork {

    private val model: MultiLayerNetwork

    init {
        val conf = NeuralNetConfiguration.Builder()
            .seed(123) // Ensure reproducibility
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .updater(Adam())
            .weightInit(WeightInit.XAVIER)
            .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
            .gradientNormalizationThreshold(1.0)
            .list()
            .layer(
                0, LSTM.Builder()
                    .nIn(1) // Input size (single value)
                    .nOut(50) // Number of units in the LSTM layer
                    .activation(Activation.TANH)
                    .build()
            )
            .layer(
                1, RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .activation(Activation.IDENTITY) // Raw output for predicted price
                    .nIn(50) // Input size from LSTM layer
                    .nOut(1) // Output size (predicted price)
                    .build()
            )
            .build()

        model = MultiLayerNetwork(conf)
        model.init()
        // Add a listener for printing score every 20 iterations during training
        model.setListeners(ScoreIterationListener(20))
    }

    override fun init() {
        // Initialization already done in the constructor
    }

    override fun params(): INDArray {
        return model.params()
    }

    override fun fit(data: DataSet) {
        model.fit(data)
    }

    override fun fit(multiDataSet: MultiDataSet) {
        // Not applicable for this model with single input and output
        throw UnsupportedOperationException("MultiDataSet fit not supported for this model")
    }

    override fun updaterState(): INDArray {
        return model.updater.stateViewArray // Use getState() method //
    }


    override fun getOptimizer(): ConvexOptimizer {
        return model.optimizer
    }

    override fun fit(iterator: DataSetIterator) {
        model.fit(iterator)
    }

    override fun fit(multiDataSetIterator: MultiDataSetIterator) {
        // Not applicable for this model with single input and output
        throw UnsupportedOperationException("MultiDataSetIterator fit not supported for this model")
    }

    override fun <T : IEvaluation<out IEvaluation<*>>?> doEvaluation(p0: DataSetIterator?, vararg p1: T): Array<T> {
        if (p0 == null || p1.isEmpty()) {
            @Suppress("UNCHECKED_CAST")
            return arrayOfNulls<IEvaluation<out IEvaluation<*>>>(0) as Array<T>
        }
        @Suppress("UNCHECKED_CAST")
        val evaluations = p1 as Array<IEvaluation<out IEvaluation<*>>> // Cast to broader type safely
        @Suppress("UNCHECKED_CAST")
        return model.doEvaluation(p0, *evaluations) as Array<T>
    }


    override fun <T : IEvaluation<out IEvaluation<*>>?> doEvaluation(
        iterator: MultiDataSetIterator,
        vararg evaluations: T
    ): Array<T> {
        throw UnsupportedOperationException("MultiDataSetIterator doEvaluation not supported for this model")
    }

    fun output(input: INDArray): INDArray {
        return model.output(input)
    }

}
