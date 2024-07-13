package org.example.util

import org.slf4j.LoggerFactory


class AlgoLogger {

    private val LOG by lazy {
        LoggerFactory.getLogger(this.javaClass.name)
    }

    fun log_info(string: String){
        LOG.info(string)
    }

    fun log_warn(string: String){
        LOG.warn(string)
    }


    fun log_trace(string: String){
        LOG.trace(string)
    }


    fun log_debug(string: String){
        LOG.debug(string)
    }

    fun log_err(string: String){
        LOG.error(string)
    }
}