package com.antonyhayek.graphartists.repository

import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.Utils
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.data.repository.ArtistRepositoryImpl
import com.antonyhayek.graphartists.domain.repository.ArtistRepository
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ArtistRepositoryImplTest {

    @RelaxedMockK
    private lateinit var mockData: ArtistsQuery.Data

    private val mockWebServer = MockWebServer()

    private lateinit var apolloClient: ApolloClient

    private lateinit var objectUnderTest: ArtistRepository

    @Before
    fun setUp() {

        MockKAnnotations.init(this)

        mockWebServer.start(8080)

        val okHttpClient = OkHttpClient.Builder()
            .dispatcher(Dispatcher(Utils.immediateExecutorService()))
            .build()

        apolloClient = ApolloClient.Builder()
            .serverUrl(mockWebServer.url("/").toString())
            .okHttpClient(okHttpClient)
            .build()

        objectUnderTest = ArtistRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

  @Test
  fun givenErrorResponse_fetchArtists_returnException() {
      val expectedError = Resource.Failure(false, 0, Exception("Something went wrong"))

      runBlocking {
          val actualResult = objectUnderTest.getArtistList("John Mayer")

          assertEquals(expectedError::class.java, actualResult!!::class.java)
      }
  }

    @Test
    fun givenErrorResponse_fetchArtistDetails_returnException() {
        val expectedError = Resource.Failure(false, 0, Exception("Something went wrong"))

        runBlocking {
            val actualResult = objectUnderTest.getArtist("QXJ0aXN0OjE0NGVmNTI1LTg1ZTktNDBjMy04MzM1LTAyYzMyZDA4NjFmMw==")

            assertEquals(expectedError::class.java, actualResult!!::class.java)
        }
    }

    @Test
    fun givenArtistDetailResponse_fetchArtistDetails_returnSuccess() {
        mockWebServer.enqueue(Utils.mockResponse("artist_details_response.json"))

        val expectedSuccess = Resource.Success(mockData)

        runBlocking {
            val actualResult = objectUnderTest.getArtist("QXJ0aXN0OjBmNDYwYmViLTYwYTItNDBiZC1hNGYwLWM5OTkwOWZhZWJlYQ==")

            assertEquals(expectedSuccess::class.java, actualResult!!::class.java)
        }
    }
}