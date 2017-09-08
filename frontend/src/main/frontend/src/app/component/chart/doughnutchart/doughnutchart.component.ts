import {Component} from '@angular/core';
import {ReportDataService} from "../../report/report.data.service";
import {Message} from "primeng/primeng";

@Component({
  selector: 'doughnut',
  templateUrl: './doughnutchart.component.html'
})
export class DoughnutChartComponent  {

  data: any;
  msgs: Message[] = [];
  amountOfTwo: number;
  amountOfTwoAndHalf: number;
  amountOfThree: number;
  amountOfThreeAndHalf: number;
  amountOfFour: number;
  amountOfFourAndHalf: number;
  amountOfFive: number;

  constructor(private reportDataService : ReportDataService) {
    this.reportDataService.getGrades()
      .subscribe(pageResponse => {
          this.amountOfTwo = pageResponse[0];
          this.amountOfTwoAndHalf = pageResponse[1];
          this.amountOfThree = pageResponse[2];
          this.amountOfThreeAndHalf = pageResponse[3];
          this.amountOfFour = pageResponse[4];
          this.amountOfFourAndHalf = pageResponse[5];
          this.amountOfFive = pageResponse[6];

          this.data = {
            labels: ['2','2.5','3','3.5','4','4.5','5'],
            datasets: [
              {
                data: [this.amountOfTwo,
                  this.amountOfTwoAndHalf,
                  this.amountOfThree,
                  this.amountOfThreeAndHalf,
                  this.amountOfFour,
                  this.amountOfFourAndHalf,
                  this.amountOfFive],
                backgroundColor: [
                  "#ff000c",
                  "#eb8c97",
                  "#fff25c",
                  "#00e8ff",
                  "#5763ff",
                  "#050fff",
                  "#1eff06",
                ],
                hoverBackgroundColor: [
                  "#ff000c",
                  "#eb8c97",
                  "#fff25c",
                  "#00e8ff",
                  "#5763ff",
                  "#050fff",
                  "#1eff06",
                ]
              }]
          };
        },
        error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
      );
  }

}
