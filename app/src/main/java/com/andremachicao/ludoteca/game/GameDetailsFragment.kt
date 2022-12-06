package com.andremachicao.ludoteca.game

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.databinding.FragmentGameDetailsBinding
import com.andremachicao.ludoteca.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class GameDetailsFragment : Fragment() {

    private lateinit var binding : FragmentGameDetailsBinding
    private val args: GameDetailsFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private lateinit var auth: FirebaseAuth
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding.gameInfo = args.gameInfo
        for (element in args.gameInfo.images){
            list.add(CarouselItem(element))
        }
        //list.add(CarouselItem(args.gameInfo.image))
        //list.add(CarouselItem("https://images.drive.com.au/driveau/image/private/c_fill,f_auto,g_auto,h_674,q_auto:eco,w_1200/ca-s3/2013/03/Suzuki-SX4-5-625x404.jpg"))
        //list.add(CarouselItem("https://www.km77.com/media/fotos/suzuki_sx4_2010_3480_1.jpg"))
        binding.carousel.addData(list)

        binding.btUpdateGame.setOnClickListener {
            Toast.makeText(context,"Entro al on click del update",Toast.LENGTH_SHORT).show()
            val goToUpdatePage = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameUpdateFragment(args.gameInfo)
            findNavController().navigate(goToUpdatePage)
        }

        binding.buttonDeleteGame.setOnClickListener {
            auth.currentUser?.email?.let { email->
                db.collection("users").document(email).collection("Games").document(args.gameInfo.id).delete().addOnSuccessListener {
                    val goToMainGameList = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                    val storageRef = storage.reference
                    val desertRef1 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_1.jpeg")
                    val desertRef2 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_2.jpeg")
                    val desertRef3 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_3.jpeg")
                    desertRef1.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    desertRef2.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    desertRef3.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    Toast.makeText(context,"Se elimino el juego",Toast.LENGTH_SHORT).show()

                    findNavController().navigate(goToMainGameList)
                }
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                findNavController().navigate(goToPrevious)
            }
        })

    }

}