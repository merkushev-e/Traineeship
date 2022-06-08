package com.testtask.traineeship.presentation.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testtask.traineeship.R
import com.testtask.traineeship.data.ContactsList.createNContacts
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
    private var currentPosition: Int? = null

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

        val itemDecoration = DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
        ContextCompat.getDrawable(requireActivity(), R.drawable.separator)
            ?.let { itemDecoration.setDrawable(it) }

        with(binding) {
            contactsRecyclerview.adapter = adapter
            contactsRecyclerview.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            contactsRecyclerview.addItemDecoration(itemDecoration)
        }

        floatingActionButton.setOnClickListener {
            val num = Random().nextInt(((maxRandom - minRandom) + 1) + minRandom)
            contact = Contact(
                num.toString(),
                getString(R.string.Name),
                getString(R.string.LastName),
                Random().nextInt(200)
            )
            if (contact != null) {
                adapter.appendItem(contact!!)
                viewModel.saveContactToDB(contact!!)

            }
            binding.EmptyTextview.visibility = View.GONE
        }

        adapter.listener = ContactListAdapter.OnListItemClickListener { showDetails(it) }
        adapter.onDeleteListener = ContactListAdapter.OnListItemDeleteClickListener { data ->
            viewModel.deleteContactFromDb(data)
        }
        adapter.fragment = this
        adapter.onItemLongClickListener =
            ContactListAdapter.OnItemLongClickListener { position, data ->
                currentPosition = position
                contact = data
            }
        binding.createContacts.setOnClickListener {
            viewModel.saveContactsToDBAndShow(createNContacts(100))
            getData()
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
                if (data != null) {
                    if (data.isEmpty()) {

                        showEmptyText()
                    } else {
                        showViewSuccess()
                        adapter.setData(data)
                    }
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


    private fun showEmptyText() {
        binding.EmptyTextview.visibility = View.VISIBLE
        binding.successLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
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
        binding.EmptyTextview.visibility = View.GONE
        binding.successLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
    }

    private fun showViewSuccess() {
        binding.EmptyTextview.visibility = View.GONE
        binding.successLayout.visibility = View.VISIBLE
        binding.loadingFrameLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showViewLoading() {
        binding.EmptyTextview.visibility = View.GONE
        binding.successLayout.visibility = View.GONE
        binding.loadingFrameLayout.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }





    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.item_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_delete -> {


                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.message)
                    .setTitle(R.string.tittle)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        currentPosition?.let {
                            adapter.removeOnPosition(it)
                            adapter.notifyItemRemoved(it)
                        }
                        contact?.let { viewModel.deleteContactFromDb(it) }
                        Toast.makeText(context, R.string.message_note_deleted, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, which -> }
                    .show()


                return true
            }
        }
        return super.onContextItemSelected(item)
    }


}