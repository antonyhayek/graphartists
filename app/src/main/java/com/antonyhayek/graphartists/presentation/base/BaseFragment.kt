package com.antonyhayek.graphartists.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.antonyhayek.graphartists.R


typealias Inflate<I> = (LayoutInflater, ViewGroup?, Boolean) -> I

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!


    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view.findViewById(R.id.root)

        setUiWindowInsets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    protected open fun setUiWindowInsets() {
        if (rootView == null) {
            return
        }

        var posTop = 0
        var posBottom = 0
        ViewCompat.setOnApplyWindowInsetsListener(rootView!!) { v, insets ->

            if (posBottom == 0) {
                posTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                posBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            }

            v.updatePadding(
                v.paddingStart,
                posTop,
                v.paddingEnd,
                posBottom
            )

            insets
        }
    }


}