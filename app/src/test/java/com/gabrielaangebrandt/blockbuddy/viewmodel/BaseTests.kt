package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

abstract class BaseTests {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    abstract fun setUpViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        setUpViewModel()
    }
}