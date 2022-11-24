package com.antonyhayek.graphartists.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antonyhayek.graphartists.MainCoroutineRule
import com.antonyhayek.graphartists.domain.repository.ArtistRepository
import com.antonyhayek.graphartists.domain.use_case.GetArtistListUseCase
import com.antonyhayek.graphartists.presentation.ui.artists.ArtistsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ArtistsViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockRepository: ArtistRepository

    @Mock
    private lateinit var mockUseCase: GetArtistListUseCase

    private lateinit var objectUnderTest: ArtistsViewModel

    val stateFlow = MutableStateFlow<ArtistsViewModel.UIEventArtists>(ArtistsViewModel.UIEventArtists.OnLoading(false))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        objectUnderTest = ArtistsViewModel(mockUseCase)
    }

    /*@Test
    fun fetchArtistList_returnLoading() = runBlockingTest {

        objectUnderTest.getArtistList("")

        assertNotEquals(stateFlow.value, ArtistsViewModel.UIEventArtists.OnLoading(true))
    }*/
}