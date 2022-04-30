package com.example.stockcomperator.ui

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockcomperator.data.model.BaseResponse
import com.example.stockcomperator.data.model.Constants
import com.example.stockcomperator.data.model.QuoteSymbols
import com.example.stockcomperator.data.network.Resource
import com.example.stockcomperator.data.network.Status
import com.example.stockcomperator.databinding.ActivityMainBinding
import com.example.stockcomperator.utils.DateAxisFormatter
import com.example.stockcomperator.utils.ItemClickListener
import com.example.stockcomperator.utils.ProgressDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: StocksAdapter
    private val mainViewModel: MainViewModel by viewModels()
    private var mainList: List<QuoteSymbols>? = mutableListOf()
    lateinit var mProgressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initLineChart()
        mainViewModel.stockListResult.observe(this, ::consumeStocksResult)
        mainViewModel.symbolListResult.observe(this, ::consumeSymbolResult)
        //by default getting month
        binding.rdbMonth.isChecked = true
    }

    private fun consumeStocksResult(resource: Resource<BaseResponse>) {
        when (resource.status) {
            Status.LOADING -> {
                showLoading()
            }
            Status.SUCCESS -> {
                hideLoading()
                mainList = resource.data?.content?.quoteSymbols
                setDataToLineChart(mainList)
                mainViewModel.getSymbolList()
            }
            Status.ERROR -> {
                hideLoading()
                handleError(resource.throwable)
            }
        }
    }


    //we can handle here different types of exceptions, but for now keeping simple and logging exception
    private fun handleError(throwable: Throwable?) {
        throwable?.printStackTrace()
    }

    private fun consumeSymbolResult(mList: List<String>?) {
        mAdapter.setList(mList.orEmpty())
    }

    private fun initView() {
        mProgressDialog = ProgressDialog(this)
        mAdapter = StocksAdapter(listOf(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rdbMonth.id -> {
                    mainViewModel.getStockList(Constants.ASSET_DATA_SOURCE_MONTH)
                }
                binding.rdbWeek.id -> {
                    mainViewModel.getStockList(Constants.ASSET_DATA_SOURCE_WEEK)
                }
            }
        }
    }


    private fun initLineChart() {

//        hide grid lines
        binding.lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        //remove right y-axis
        binding.lineChart.axisRight.isEnabled = false
        //remove legend
        binding.lineChart.legend.isEnabled = true
        //remove description label
        binding.lineChart.description.isEnabled = false
        //add animation
        binding.lineChart.animateX(1000, Easing.EaseInSine)
        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set custom formatter for date
        xAxis.valueFormatter = DateAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 0f

    }

    private fun setDataToLineChart(chartData: List<QuoteSymbols>?) {
        //if data is null then invalidate chart and return
        if (chartData == null) {
            binding.lineChart.invalidate()
            return
        }
        val dataSets = ArrayList<ILineDataSet>()
        for ((z, data: QuoteSymbols) in chartData.withIndex()) {
            val entries: ArrayList<Entry> = ArrayList()
            val firstData = data.closures[0]
            data.closures.forEachIndexed { index, item ->
                val percentage = ((item - firstData) / firstData) * 100
                entries.add(Entry(data.timestamps[index].toFloat(), percentage.toFloat()))
            }
            val lineDataSet = LineDataSet(entries, data.symbol)
            val color: Int = Constants.COLORS[z % Constants.COLORS.size]
            lineDataSet.color = color
            lineDataSet.setCircleColor(color)
            dataSets.add(lineDataSet)
        }

        val data = LineData(dataSets)
        binding.lineChart.data = data
        binding.lineChart.invalidate()
    }


    override fun onClickListener(position: Int) {
        val selectedSymbols = mAdapter.getCheckedList()
        val filteredResult: List<QuoteSymbols>? = mainList?.filter {
            selectedSymbols.contains(it.symbol)
        }
        setDataToLineChart(filteredResult)
    }

    private fun hideLoading() {
        mProgressDialog.cancel()
    }

    private fun showLoading() {
        mProgressDialog.show()
    }


}