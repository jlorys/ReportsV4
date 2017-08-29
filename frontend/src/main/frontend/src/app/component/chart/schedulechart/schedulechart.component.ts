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
        "title": "Całodniowe wydarzenie",
        "start": "2017-08-09"
      },
      {
        "title": "Długie wydarzenie",
        "start": "2017-08-07",
        "end": "2017-08-10"
      },
      {
        "title": "Powtarzające się wydarzenie",
        "start": "2017-08-14T16:00:00"
      },
      {
        "title": "Powtarzające się wydarzenie",
        "start": "2017-08-16T16:00:00"
      },
      {
        "title": "Konferencja",
        "start": "2017-08-11",
        "end": "2017-08-13"
      }
    ];
  }
}
