package com.example.funiture_shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.funiture_shop.databinding.ActivityMainBinding
import com.example.funiture_shop.helper.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var width = 0F
    private var height = 0F
    private var distanceX = 0F
    private var distanceY = 0F
    private var movedX: Float = 0F
    private var movedY: Float = 0F
    private var a = 0F
    private var b = 0F


    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpDraggableButton(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun setUpDraggableButton(navController: NavController) {
        val displayMetrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        width = displayMetrics.widthPixels.toFloat()
        binding.constraint.post {
            height = binding.constraint.height.toFloat()
        }


        binding.cartButton.setOnTouchListener { _, event ->
            val action = event.action
            var xDown = 0F
            var yDown = 0F
            when (action) {

                MotionEvent.ACTION_DOWN -> {
                    xDown = event.x
                    yDown = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    movedX = event.x
                    movedY = event.y

                    distanceX = movedX - xDown
                    distanceY = movedY - yDown
                    binding.apply {
                        cartButton.x = cartButton.x + distanceX
                        cartButton.y = cartButton.y + distanceY
                    }

                }

                MotionEvent.ACTION_UP -> {
                    if (a == movedX && b == movedY) {
                        navigateToCart(navController)
                    } else {
                        a = movedX
                        b = movedY
                        binding.apply {
                            if (cartButton.x >= width - 100) {
                                cartButton.x = width - 100 * 3
                            }
                            if (cartButton.x <= 0) {
                                cartButton.x = 0f
                            }

                            if (cartButton.y <= 0) {
                                cartButton.y = 0f
                            }

                            if (cartButton.y >= height - 100) {
                                cartButton.y = height - 100 * 3
                            }
                        }
                    }


                }

                else -> {

                }
            }

            true
        }

    }

    private fun navigateToCart(navController: NavController) {
        when (navController.currentDestination?.id) {
            R.id.HomeFragment -> {
                navController.navigate(R.id.action_FirstFragment_to_cartFragment)
            }

            R.id.accountFragment -> {
                navController.navigate(R.id.action_accountFragment_to_cartFragment)
            }

            R.id.productDetailFragment -> {
                navController.navigate(R.id.action_productDetailFragment_to_cartFragment)
            }

            R.id.ratingFragment -> {
                navController.navigate(R.id.action_ratingFragment_to_cartFragment)
            }

            R.id.searchFragment -> {
                navController.navigate(R.id.action_searchFragment_to_cartFragment)
            }

            R.id.chatFragment -> {
                navController.navigate(R.id.action_chatFragment_to_cartFragment)
            }

            R.id.historyOrderFragment -> {
                navController.navigate(R.id.action_historyOrderFragment_to_cartFragment)
            }

            R.id.newOrderInfoFragment -> {
                navController.navigate(R.id.action_newOrderInfoFragment_to_cartFragment)
            }

            R.id.chooseLocationFragment -> {
                navController.navigate(R.id.action_chooseLocationFragment_to_cartFragment)
            }

            R.id.supportFragment -> {
                navController.navigate(R.id.action_supportFragment_to_cartFragment)
            }

            else -> {
                Toast.makeText(this, "Not Support! :((", Toast.LENGTH_SHORT).show()
            }
        }
    }

}