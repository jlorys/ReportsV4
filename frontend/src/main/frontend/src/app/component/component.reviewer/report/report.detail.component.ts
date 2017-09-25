import {Component, Input, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Message, SelectItem} from "primeng/primeng";
import {ReviewerReportDataService} from "./report.data.service";
import {saveAs as importedSaveAs} from "file-saver";
import {Report} from "../../component.admin/report/report";
import {Laboratory} from "../../component.admin/laboratory/laboratory";

@Component({
  selector: 'reviewerReports-detail',
  templateUrl: 'report.detail.component.html',
})
export class ReviewerReportsDetailComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  report: Report;

  private params_subscription: any;

  msgs: Message[] = [];

  laboratory: Laboratory;

  grade: string;
  grades: SelectItem[];

  constructor(private route: ActivatedRoute, private reviewerReportDataService: ReviewerReportDataService) {
    this.grades = [];
    this.grades.push({label:'2', value:2});
    this.grades.push({label:'2.5', value:2.5});
    this.grades.push({label:'3', value:3});
    this.grades.push({label:'3.5', value:3.5});
    this.grades.push({label:'4', value:4});
    this.grades.push({label:'4.5', value:4.5});
    this.grades.push({label:'5', value:5});

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for reviewerReports-detail ' + id);

        this.reviewerReportDataService.getReport(id)
          .subscribe(report => {
              this.report = report;
              this.laboratory = report.laboratory;
              this.grade = report.grade;
            },
            error => this.msgs.push({severity: 'error', summary: 'Constructor error', detail: error})
          );
    });
  }

  ngOnDestroy() {
    this.params_subscription.unsubscribe();
  }

  onUpdate() {
    this.msgs = []; //this line fix disappearing of messages

    this.report.laboratory = this.laboratory;
    this.report.grade = this.grade;

    if(this.laboratory){
      this.reviewerReportDataService.update(this.report).subscribe(
        report => {
          this.report = report;
          this.msgs.push({severity: 'info', summary: 'Zapisano', detail: 'OK!'})
        },
        error => this.msgs.push({severity: 'error', summary: 'Nie można zapisać', detail: error})
      );
    } else {
      this.msgs.push({severity: 'error', summary: 'Nie można zapisać', detail: "Raport musi mieć określone laboratorium, by go ocenić!"})
    }

  }

  onDownloadFile() {
    this.reviewerReportDataService.downloadFile(this.report.id).subscribe(blob => {
        importedSaveAs(blob, this.report.fileName + this.report.fileExtension);
      }
    )
  }

}
