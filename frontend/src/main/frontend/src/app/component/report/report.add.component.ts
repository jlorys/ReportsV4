import {Report} from "./report";
import {Component, Input, OnDestroy} from "@angular/core";
import {AppUser} from "../users/user";
import {ActivatedRoute, Router} from "@angular/router";
import {Message, SelectItem} from "primeng/primeng";
import {ReportDataService} from "./report.data.service";
import {AppUserDataService} from "../users/user.data.service";
import {saveAs as importedSaveAs} from "file-saver";
import {LaboratoryDataService} from "../laboratory/laboratory.data.service";
import {Laboratory} from "../laboratory/laboratory";

@Component({
  templateUrl: 'report.add.component.html',
  selector: 'reports-add',
})
export class ReportsAddComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  report: Report;
  sourceUsers: AppUser[] = [];

  private params_subscription: any;

  msgs: Message[] = [];
  filePath: string = "/uploaded_files";

  laboratory: Laboratory;
  sourceLaboratoriesSelectItems: SelectItem[];

  constructor(private route: ActivatedRoute, private router: Router, private reportService: ReportDataService, private appUserDataService: AppUserDataService, private laboratoryDataService: LaboratoryDataService) {
    this.sourceLaboratoriesSelectItems = [];
    this.sourceLaboratoriesSelectItems.push({label: '--------------------------', value: null});

    laboratoryDataService.findAll().subscribe(subject => subject.forEach((value, index, array) => this.sourceLaboratoriesSelectItems.push({
        label: value.name,
        value: value
      })),
      error => this.msgs.push({
        severity: 'error',
        summary: 'Constructor laboratory of report error',
        detail: error
      }));

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for reports-add ' + id);
      if (id === 'add') {
        this.report = new Report();
        this.laboratory = null;

        appUserDataService.findAllAppUsersWhichDoNotHaveReportWithThisId(0).//0 means find all
        subscribe(users => this.sourceUsers = users,
          error => this.msgs.push({severity: 'error', summary: 'Constructor user reports error', detail: error}))

      } else {
        this.reportService.getReport(id)
          .subscribe(report => {
              this.report = report;
              this.laboratory = report.laboratory;

              appUserDataService.findAllAppUsersWhichDoNotHaveReportWithThisId(this.report.id).subscribe(users => this.sourceUsers = users,
                error => this.msgs.push({severity: 'error', summary: 'Constructor user reports error', detail: error}))

            },
            error => this.msgs.push({severity: 'error', summary: 'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
    this.params_subscription.unsubscribe();
  }

  onUpdate() {
    this.msgs = []; //this line fix disappearing of messages

    this.report.laboratory = this.laboratory;

    this.reportService.update(this.report).subscribe(
      report => {
        this.report = report;
        this.msgs.push({severity: 'info', summary: 'Zapisano', detail: 'OK!'})
      },
      error => this.msgs.push({severity: 'error', summary: 'Nie można zapisać', detail: error})
    );
  }

  myUploader(event) {
    this.msgs = []; //this line fix disappearing of messages

    this.report.filePath = this.filePath;
    let fullFileName = event.files[0].name;
    this.report.fileExtension = '.' + fullFileName.split('.').pop();
    this.report.fileName = fullFileName.split('.').shift();

    this.reportService.update(this.report).subscribe(
      report => {
        this.report = report;

        let xhr = new XMLHttpRequest(), formData = new FormData();
        formData.append("file", event.files[0], this.report.id + "_r_" + fullFileName);
        xhr.open("POST", '/api/reports/upload', true);
        xhr.send(formData);

        this.msgs.push({severity: 'info', summary: 'Zapisano', detail: 'OK!'})
      },
      error => this.msgs.push({severity: 'error', summary: 'Nie można zapisać', detail: error})
    );
  }

  onDownloadFile() {
    this.reportService.downloadFile(this.report.id).subscribe(blob => {
        importedSaveAs(blob, this.report.fileName + this.report.fileExtension);
      }
    )
  }

}
