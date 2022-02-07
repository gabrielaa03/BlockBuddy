package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
abstract class BaseTests {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    protected val testCoroutineScope by lazy { TestCoroutineScope(testDispatcher) }

    abstract fun setUpViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        setUpViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }
}