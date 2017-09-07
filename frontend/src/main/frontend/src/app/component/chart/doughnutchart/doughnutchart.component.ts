import {Component} from '@angular/core';

@Component({
  selector: 'doughnut',
  templateUrl: './doughnutchart.component.html'
})
export class DoughnutChartComponent  {

  data: any;

  constructor() {
    this.data = {
      labels: ['2','2.5','3','3.5','4','4.5','5'],
      datasets: [
        {
          data: [300, 50, 100, 100, 30, 70, 90],
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
  }

}
