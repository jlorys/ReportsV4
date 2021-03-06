import {Component, OnInit} from '@angular/core';
import {PageResponse} from "../../../support/paging";
import {Laboratory} from "../../component.admin/laboratory/laboratory";
import {Message} from "primeng/primeng";
import {UserLaboratoryDataService} from "../../component.user/laboratory/laboratory.data.service";

@Component({
  selector: 'schedule',
  templateUrl: './schedulechart.component.html'
})
export class ScheduleChartComponent implements OnInit{

  events: MyEvent[];
  // list is paginated
  currentPage: PageResponse<Laboratory> = new PageResponse<Laboratory>(0, 0, []);
  // basic search criterias (visible if not in 'sub' mode)
  example: Laboratory = new Laboratory();
  msgs: Message[] = [];

  constructor(private userLaboratoryDataService : UserLaboratoryDataService) {}

  ngOnInit() {
    this.events = [];
    this.userLaboratoryDataService.getPage(this.example, {first: 0, rows: 100, sortField: null, sortOrder: null, filters: null, multiSortMeta: null})
      .subscribe(pageResponse => {
          pageResponse.content.forEach((value, index, array) => {
            this.events.push({
              "id": index,
              "title": value.name + " - data laboratoriów",
              "start": this.toUTCDate(new Date(value.labDate)).toString(),
              "end": this.toUTCDate(new Date(value.labDate)).toString()});
            this.events.push({
              "id": index,
              "title": value.name + " - data oddania sprawozdania",
              "start": this.toUTCDate(new Date(value.returnReportDate)).toString(),
              "end": this.toUTCDate(new Date(value.returnReportDate)).toString()});
            this.events.push({
              "id": index,
              "title": value.name + " - ostateczna data oddania sprawozdania",
              "start": this.toUTCDate(new Date(value.finalReturnReportDate)).toString(),
              "end": this.toUTCDate(new Date(value.finalReturnReportDate)).toString()});
          })
        },
        error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
      );
  }

  toUTCDate(date: Date){
  var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
  return _utc;
  };

  event: MyEvent;
  dialogVisible: boolean = false;
  handleEventClick(e) {
    this.event = new MyEvent();
    this.event.title = e.calEvent.title;

    let start = e.calEvent.start;
    let end = e.calEvent.end;
    if(e.view.name === 'month') {
      start.stripTime();
    }

    if(end) {
      end.stripTime();
      this.event.end = end.format();
    }

    this.event.id = e.calEvent.id;
    this.event.start = start.format();
    this.dialogVisible = true;
  }
}

export class MyEvent {
  id: number;
  title: string;
  start: string;
  end: string;
}
