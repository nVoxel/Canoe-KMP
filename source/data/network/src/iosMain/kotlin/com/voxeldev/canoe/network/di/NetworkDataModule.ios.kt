package com.voxeldev.canoe.network.di

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

/**
 * @author nvoxel
 */
actual fun getKtorEngineFactory(): HttpClientEngineFactory<HttpClientEngineConfig> = Darwin
