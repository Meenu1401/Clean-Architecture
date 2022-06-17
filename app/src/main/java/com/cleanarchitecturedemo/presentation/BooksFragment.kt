package com.cleanarchitecturedemo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cleanarchitecturedemo.LayoutUtils
import com.cleanarchitecturedemo.R
import com.cleanarchitecturedemo.databinding.FragmentBooksBinding
import com.cleanarchitecturedemo.entities.BookWithStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksFragment : Fragment() {

    val binding: FragmentBooksBinding by viewBinding(CreateMethod.INFLATE)

    private lateinit var booksAdapter: BookAdapter

    private val booksViewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booksAdapter = BookAdapter(requireContext(), object : BookAdapter.ActionClickListener {
            override fun bookmark(book: BookWithStatus) {
                booksViewModel.bookmark(book)
            }

            override fun unbookmark(book: BookWithStatus) {
                booksViewModel.unbookmark(book)
            }
        })

        booksViewModel.getBooks("Robert C. Martin")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.apply {
            launch {

                // The block passed to repeatOnLifecycle is executed when the lifecycle
                // is at least STARTED and is cancelled when the lifecycle is STOPPED.
                // It automatically restarts the block when the lifecycle is STARTED again.

                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    booksViewModel.books.collectLatest {
                        booksAdapter.submitUpdate(it)
                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    booksViewModel.dataLoading.collectLatest { loading ->
                        binding.apply {
                            when (loading) {
                                true -> LayoutUtils.crossFade(pbLoading, rvBooks)
                                false -> LayoutUtils.crossFade(rvBooks, pbLoading)
                            }
                        }
                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    booksViewModel.error.collectLatest {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.an_error_has_occurred, it),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }


        observeFlowWithLife(booksViewModel.error) {
            Toast.makeText(
                requireContext(),
                getString(R.string.an_error_has_occurred, it),
                Toast.LENGTH_SHORT
            ).show()
        }

        booksViewModel.errorLv.observe(viewLifecycleOwner){
            Toast.makeText(
                requireContext(),
               "Livedata"+ getString(R.string.an_error_has_occurred, it),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvBooks.apply {
            layoutManager =
                GridLayoutManager(requireContext(), COLUMNS_COUNT)
            adapter = booksAdapter
        }
    }

    companion object {
        const val COLUMNS_COUNT = 2
    }
}

fun <T> Fragment.observeFlowWithLife(flow: SharedFlow<T>, block: (T) ->Unit)
{
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collectLatest {
                block(it)
            }
        }
    }
}