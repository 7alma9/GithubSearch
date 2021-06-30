package com.nxb.githubsearchdemo

import org.mockito.Mockito


inline fun < reified T> mockTest():T = Mockito.mock(T::class.java)