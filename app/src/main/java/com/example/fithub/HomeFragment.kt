package com.example.fithub

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    return inflater.inflate(R.layout.fragment_home, container, false)}

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val compactCalendar: CompactCalendarView = view?.findViewById(R.id.calendarViewCalendar)!!
        val dateFormatMonth = SimpleDateFormat("mm yyyy", Locale.getDefault())

        compactCalendar.setUseThreeLetterAbbreviation(true)

        //val date:Date = dateFormatMonth.get2DigitYearStart()
        //textViewMonth.text = dateFormatMonth.format(date)

        //set an event
        val event1 = Event(Color.BLUE, 1578537000000L, "test")
        val event2 = Event(Color.BLUE, 1578537000000L, "test2")
        compactCalendar.addEvent(event1)
        compactCalendar.addEvent(event2)

        compactCalendar.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                //Toast.makeText(context, dateClicked.toString(), Toast.LENGTH_SHORT).show()
                if(dateClicked.toString().compareTo("Thu Jan 09 00:00:00 GMT+08:00 2020") == 0){
                    Toast.makeText(context, event1.data.toString(), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "No event", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
            }
        })
    }
}
