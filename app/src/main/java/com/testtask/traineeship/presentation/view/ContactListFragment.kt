package com.testtask.traineeship.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testtask.traineeship.R
import com.testtask.traineeship.databinding.FragmentContactListBinding
import com.testtask.traineeship.domain.AppState
import com.testtask.traineeship.domain.model.Contact
import com.testtask.traineeship.presentation.view.ContactDetailsFragment.Companion.CONTACT_RESULT
import com.testtask.traineeship.presentation.view.ContactDetailsFragment.Companion.CONTACT_RESULT_BUNDLE
import com.testtask.traineeship.presentation.view.ContactDetailsFragment.Companion.CONTACT_RESULT_PREV_VAL
import com.testtask.traineeship.presentation.viewmodel.ContactListAdapter
import com.testtask.traineeship.presentation.viewmodel.ContactListViewModel
import kotlinx.android.synthetic.main.fragment_contact_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ContactListFragment : Fragment() {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private val minRandom = 111111111
    private val maxRandom = 999999999
    private var contact: Contact? = null
    private var prevNumber: String? = null

    private val viewModel: ContactListViewModel by viewModel()

    private val adapter: ContactListAdapter by lazy {
        ContactListAdapter()
    }

    private var contactFromFragment: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CONTACT_RESULT) { _, bundle ->
            contactFromFragment = bundle.getParcelable(CONTACT_RESULT_BUNDLE)

            prevNumber = bundle.getString(CONTACT_RESULT_PREV_VAL)

            if (contactFromFragment?.number != null
                && contactFromFragment?.name != null
                && contactFromFragment?.lastName != null
                && prevNumber != null
            ) {
                viewModel.updateContactInDb(
                    contactFromFragment!!.number,
                    contactFromFragment!!.name,
                    contactFromFragment!!.lastName,
                    prevNumber!!
                )
            }
            getData()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        _binding = FragmentContactListBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {

        with(binding) {
            contactsRecyclerview.adapter = adapter
            contactsRecyclerview.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        floatingActionButton.setOnClickListener {
           val num =  Random().nextInt(((maxRandom - minRandom) + 1) + minRandom)
            contact = Contact(num.toString(), getString(R.string.Name), getString(R.string.LastName))
            if (contact != null) {
                adapter.appendItem(contact!!)
                viewModel.saveContactToDB(contact!!)
            }

        }

        adapter.listener = ContactListAdapter.OnListItemClickListener { showDetails(it) }
        adapter.onDeleteListener = ContactListAdapter.OnListItemDeleteClickListener { data ->
            viewModel.deleteContactFromDb(data)
        }
        getData()

    }

    private fun getData() {
        viewModel.getData(true)
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Success -> {
                val data = appState.data
                if (data == null || data.isEmpty()) {

                    showErrorScreen("Ошибка")
                } else {
                    showViewSuccess()
                    adapter.setData(data)
                }
            }

            is AppState.Loading -> {
                showViewLoading()
            }

            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
        }
    }

    private fun showDetails(data: Contact) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ContactDetailsFragment.newInstance(data))
            .addToBackStack("TAG")
            .commit()
    }


    private fun showErrorScreen(error: String?) {
        showViewError()
        binding.errorTextview.text =
            error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            viewModel.getData(true)
            viewModel.liveData.observe(viewLifecycleOwner) { appState ->
                renderData(appState)
            }
        }
    }

    private fun showViewError() {
        binding.successLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
    }

    private fun showViewSuccess() {
        binding.successLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showViewLoading() {
        binding.successLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }


}