import {Component} from '@angular/core';

@Component({
  selector: 'schedule',
  templateUrl: './schedulechart.component.html'
})
export class ScheduleChartComponent{

  events: any[];

  ngOnInit() {
    this.events = [
      {
        "title": "All Day Event",
        "start": "2017-08-09"
      },
      {
        "title": "Long Event",
        "start": "2017-08-07",
        "end": "2017-08-10"
      },
      {
        "title": "Repeating Event",
        "start": "2017-08-14T16:00:00"
      },
      {
        "title": "Repeating Event",
        "start": "2017-08-16T16:00:00"
      },
      {
        "title": "Conference",
        "start": "2016-01-11",
        "end": "2016-01-13"
      }
    ];
  }
}
