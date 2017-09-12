import {Component, Input, OnDestroy} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {Laboratory} from "../../component.admin/laboratory/laboratory";
import {Report} from "../../component.admin/report/report";
import {Subject} from "../../component.admin/subject/subject";
import {UserLaboratoryDataService} from "app/component/component.user/laboratory/laboratory.data.service";

@Component({
  templateUrl: 'laboratory.detail.component.html',
  selector: 'userLaboratories-detail',
})
export class UserLaboratoryDetailComponent implements OnDestroy {

  @Input() header = "Moje sprawozdania z tego laboratorium...";
  laboratory : Laboratory;
  laboratoryReports: Report[] = [];

  private params_subscription: any;

  msgs: Message[] = [];

  subject: Subject;

  labDate: Date;
  returnReportDate: Date;
  finalReturnReportDate: Date;

  constructor(private route: ActivatedRoute, private router: Router, private userLaboratoryDataService: UserLaboratoryDataService) {

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for userLaboratories-detail ' + id);

      this.userLaboratoryDataService.getLaboratory(id)
          .subscribe(laboratory => {
              this.laboratory = laboratory;
              this.laboratoryReports = laboratory.reports;
              this.subject = laboratory.subject;
              this.labDate = new Date(laboratory.labDate);
              this.returnReportDate = new Date(laboratory.returnReportDate);
              this.finalReturnReportDate = new Date(laboratory.finalReturnReportDate);
            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
    });
  }

  ngOnDestroy() {
      this.params_subscription.unsubscribe();
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userReports', id]);
  }
}
