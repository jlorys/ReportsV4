import {Report} from "./report";
import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {AppUser} from "../users/user";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {ReportDataService} from "./report.data.service";
import {AppUserDataService} from "../users/user.data.service";
import {AppComponent} from "../app/app.component";

@Component({
  moduleId: module.id,
  templateUrl: 'report.add.component.html',
  selector: 'reports-add',
})
export class ReportsAddComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  report : Report;
  sourceUsers: AppUser[] = [];

  private params_subscription: any;

  @Output() onSaveClicked = new EventEmitter<Report>();
  @Output() onCancelClicked = new EventEmitter();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private reportService: ReportDataService, private appUserDataService: AppUserDataService,
              private appComponent: AppComponent) {

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for reports-add ' + id);
      if (id === 'add') {
        this.report = new Report();

        appUserDataService.findAllAppUsersWhichDoNotHaveReportWithThisId(0). //0 means find all
        subscribe(users => this.sourceUsers = users,
          error => this.msgs.push({severity:'error', summary:'Constructor user reports error', detail: error}))

      } else {
        this.reportService.getReport(id)
          .subscribe(report => {
              this.report = report;

              appUserDataService.findAllAppUsersWhichDoNotHaveReportWithThisId(this.report.id).
              subscribe(users => this.sourceUsers = users,
                error => this.msgs.push({severity:'error', summary:'Constructor user reports error', detail: error}))

            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
      this.params_subscription.unsubscribe();
  }

  onSave() {
    this.reportService.update(this.report).
    subscribe(
      report => {
        this.report = report;
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity:'info', summary:'Saved OK', detail: 'Angular Rocks!'})
      },
      error => this.msgs.push({severity:'error', summary:'Could not save', detail: 'Angular Rocks!'})
    );
  }
}
