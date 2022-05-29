package com.testtask.traineeship

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testtask.traineeship.databinding.FragmentTask2Binding
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL
import java.util.*



class Task2Fragment : Fragment() {

    private var _binding: FragmentTask2Binding? = null
    private val binding get() = _binding!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main + coroutineExceptionHandler)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task2, container, false)
        _binding = FragmentTask2Binding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireActivity()

        binding.button.setOnClickListener {


            val urlImage: URL = try {
                URL(binding.editText.text.toString())
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.error_input), Toast.LENGTH_SHORT).show()
                Log.d(TASK2, getString(R.string.error), e)
                URL("https://" + binding.editText.text.toString())
            }
            it.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE

            val result = scope.async(Dispatchers.IO) {
                BitmapFactory.decodeStream(urlImage.openStream())
            }

            scope.launch {
                val bitmap = result.await()
                bitmap?.apply {
                    val savedUri: Uri? = saveToInternalStorage(context)
                    binding.imageView.setImageURI(savedUri)
                }
                it.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, throwable.toString(), Toast.LENGTH_LONG).show()
        binding.button.isEnabled = true
        binding.progressBar.visibility = View.INVISIBLE
    }


    fun Bitmap.saveToInternalStorage(context: Context): Uri? {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        return try {
            val stream: OutputStream = FileOutputStream(file)
            compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            Uri.parse(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {

        private const val TASK2 = "TASK2"

        @JvmStatic
        fun newInstance() = Task2Fragment()
    }
}